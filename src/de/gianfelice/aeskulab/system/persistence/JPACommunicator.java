package de.gianfelice.aeskulab.system.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;

import de.gianfelice.aeskulab.system.utils.PropertyUtil;

/**
 * An implementation of the DBCommunicator interface. This class acts as a layer
 * between the application logic and the database. This class's responsibilities
 * are to connect to the database and manage the objects which are stored to or
 * deleted/fetched from the database. This Communicator reads the properties for
 * accessing the database out of an text-file to provide dynamic selection of´
 * the MySQL-database.
 * 
 * @author  Kim (http://code.google.com/p/vaadin-appfoundation)
 * @author  Matthias Gianfelice
 * @version 1.2.3
 */
public class JPACommunicator implements DBCommunicator {

	// ------------------------------ Attribute(s) -----------------------------
	/** Used to interact with the persistence context. */
	protected EntityManagerFactory emf = null;
	
	/**
	 * Store the {@link EntityManager} in a {@link ThreadLocal} variable to
	 * avoid multithread problems.
	 */
	protected ThreadLocal<EntityManager> em = new ThreadLocal<EntityManager>();
	
	// ----------------------------- Constructor(s) ----------------------------
	/**
	 * A constructor that does nothing. {@link JPACommunicator#init(String)}
	 * should be called after constructing a component with this constructor.
	 * Alternatively see {@link JPACommunicator#JPACommunicator(String)}.
	 */
	public JPACommunicator() {}
	
	/**
	 * Takes as input the persistence-unit name and creates an entity manager
	 * factory based on that name.
	 * @param name Persistence-unit name (defined in the
	 *             {@code persistence.xml})
	 */
	public JPACommunicator(String name) {
		init(name);
	}
	
	// ------------------------------- Method(s) -------------------------------
	/**
	 * Creates an URL to the database out of the given parameters.
	 *
	 * @param server the server
	 * @param port the port
	 * @param data the data
	 * @return        An URL as String which is used to access the database
	 */
	public static String getDataUrl(String server, String port, String data) {
		return "jdbc:mysql://" + server + ":" + port + "/" + data;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void init(String name) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		String server = PropertyUtil.getProperty(PropertyUtil.MYSQL_SERVER);
		String port = PropertyUtil.getProperty(PropertyUtil.MYSQL_PORT);
		String database = PropertyUtil.getProperty(PropertyUtil.MYSQL_DATABASE);
		
		map.put(PersistenceUnitProperties.JDBC_USER,
				PropertyUtil.getProperty(PropertyUtil.MYSQL_USER));
		map.put(PersistenceUnitProperties.JDBC_PASSWORD,
				PropertyUtil.getProperty(PropertyUtil.MYSQL_PASSWORD));
		map.put(PersistenceUnitProperties.JDBC_URL,
				getDataUrl(server, port, database));

        emf = Persistence.createEntityManagerFactory(name, map);
		
	}
	
	/**
	 * Returns an instance of the {@link EntityManager} which is open for use.
	 * 
	 * @return An instance of the {@link EntityManager}
	 */
	protected EntityManager getEntityManager() {
        if ((em.get() == null || !em.get().isOpen()) && emf != null)
            em.set(emf.createEntityManager());
        
        return em.get();
    }
	
	/**
	 * This method creates a {@link Query} object from the given entity class.
	 *
	 * @param <A> the generic type
	 * @param c the c
	 * @param em the em
	 * @return     An instance of the {@link Query} object for the given entity
	 * class
	 */
	private <A extends PersistentObject> Query generateQuery(Class<A> c,
            EntityManager em) {
        ExpressionBuilder builder = new ExpressionBuilder();
        JpaEntityManager jpaEm = JpaHelper.getEntityManager(em);
        Query query = jpaEm.createQuery(builder, c);
        
        return query;
    }
	
	/**
	 * This method creates a {@link Query} object from the given query string
	 * and the given parameters.
	 *
	 * @param queryStr the query str
	 * @param parameters the parameters
	 * @param em the em
	 * @return            An instance of the {@link Query} object created out of
	 * the given query string and parameters
	 */
	private Query generateQuery(String queryStr, Map<String, Object> parameters,
			EntityManager em) {
        Query query = em.createQuery(queryStr);
        if (parameters != null) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        
        return query;
    }
	
	/**
	 * Copies all field values recursively from {@code object2} to.
	 *
	 * @param <A>     The type of object
	 * @param object  The object to which we are copying
	 * @param object2 The object from which we are copying
	 * @param c       The class we are currently processing
	 * {@code object}.
	 */
	private <A extends PersistentObject> void copyFieldsRecursively(A object,
            A object2, Class<?> c) {
		
		// If class is null, then there is nothing left to copy
        if (c == null) return;
        
        // Get all the field in this class. NOTE: getDeclaredFields() returns
        // all fields (also private), but not the fields of the superclass(es).
        Field[] fields = c.getDeclaredFields();
        
        // Check all fields
        for (Field field : fields) {
            	
        	// If the field is transient, static or final, it's not interesting
            if (!field.isAnnotationPresent(Transient.class)
            		&& !Modifier.isStatic(field.getModifiers())
                    && !Modifier.isFinal(field.getModifiers())) {

            	// Force the field to be accessible and copy it
            	field.setAccessible(true);
                try {
                	field.set(object, field.get(object2));
                } catch (Exception e) {
                	e.printStackTrace();
                }
                
            }
            
        }

        // Now do the same with the superclass
        copyFieldsRecursively(object, object2, c.getSuperclass());

    }

	/**
	 * Creates the query for selecting a specific field's value from entities.
	 *
	 * @param c the c
	 * @param field the field
	 * @param whereClause the where clause
	 * @return             A query for selecting the given field
	 */
    private String createSelectFieldQuery(Class<? extends PersistentObject> c,
            String field, String whereClause) {
        String queryStr = "SELECT p." + field + " FROM " + c.getSimpleName()
                + " p";
        if (whereClause != null) queryStr += " WHERE " + whereClause;
        
        return queryStr;
    }

    /**
     * {@inheritDoc}
     */
	public <A extends PersistentObject> A find(Class<A> c, Long id) {
		EntityManager em = getEntityManager();
		
		try {
			return em.find(c, id);
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	public <A extends PersistentObject> List<A> list(Class<A> c) {
		EntityManager em = getEntityManager();
		
		try {
			Query query = generateQuery(c, em);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	public <A extends PersistentObject> List<A> list(String queryStr,
			Map<String, Object> parameters) {
		EntityManager em = getEntityManager();
		
		try {
			Query query = generateQuery(queryStr, parameters, em);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	public <A extends PersistentObject> List<A> list(String queryStr,
			Map<String, Object> parameters, int startIndex, int amount) {
		EntityManager em = getEntityManager();
		
		try {
			Query query = generateQuery(queryStr, parameters, em);
			query.setFirstResult(startIndex).setMaxResults(amount);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	public <A extends PersistentObject> A find(String queryStr,
			Map<String, Object> parameters) {
		EntityManager em = getEntityManager();
		
		try {
			Query query = em.createQuery(queryStr);
			if (parameters != null) {
				for (Entry<String, Object> entry : parameters.entrySet()) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			
			return (A) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	public void store(PersistentObject object) {
		EntityManager em = getEntityManager();
		
		try {
			if (!em.getTransaction().isActive()) em.getTransaction().begin();
			
			if (object.getId() != null) {
				em.merge(object);
			} else {
				em.persist(object);
			}
			em.getTransaction().commit();

			refresh(object);
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	public <A extends PersistentObject> void storeAll(Collection<A> objects) {
		EntityManager em = getEntityManager();
		
		try {
			em.getTransaction().begin();
			
			for (PersistentObject object : objects) {
				if (object.getId() != null) {
					em.merge(object);
				} else {
					em.persist(object);
				}
			}
			
			em.getTransaction().commit();
			
			for (PersistentObject object : objects) {
				refresh(object);
			}
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	public void delete(PersistentObject object) {
		if (object == null) throw new IllegalArgumentException();
		if (object.getId() == null) return;
		
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			Object entity = em.find(object.getClass(), object.getId());
			em.remove(entity);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	public <A extends PersistentObject> void deleteAll(Collection<A> objects) {
		if (objects == null) {
			throw new IllegalArgumentException();
		}
		
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			for (A object : objects) {
				if (object.getId() == null) continue;
				
				Object entity = em.find(object.getClass(), object.getId());
				if (entity != null) em.remove(entity);
			}
			
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	public <A extends PersistentObject> void refresh(A object) {
		boolean closeEm = (em.get() == null || !em.get().isOpen());
		EntityManager em = getEntityManager();
		
		A object2 = (A) em.find(object.getClass(), object.getId());
		em.refresh(object2);
		
		copyFieldsRecursively(object, object2, object.getClass());
		
		if (closeEm) close();
	}

	/**
     * {@inheritDoc}
     */
	public void close() {
		if (em.get() != null && em.get().isOpen()) {
			em.get().clear();
			em.get().close();
			em.set(null);
		}
	}

	/**
     * {@inheritDoc}
     */
	public void kill() {
		if (em.get() != null && em.get().isOpen()) em.get().close();
		if (emf != null) emf.close();
	}

	/**
     * {@inheritDoc}
     */
	public Long count(Class<? extends PersistentObject> c) {
		if (c == null) throw new IllegalArgumentException();
		
		EntityManager em = getEntityManager();
		try {
			String queryStr = "SELECT COUNT(p.id) FROM " + c.getSimpleName()
					+ " p";
			Query query = em.createQuery(queryStr);
			
			return (Long) query.getSingleResult();
		} catch (NoResultException e) {
			return -1L;
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	public Long count(Class<? extends PersistentObject> c, String whereClause,
			Map<String, Object> parameters) {
		if (c == null || whereClause == null)
			throw new IllegalArgumentException();
		
		EntityManager em = getEntityManager();
		try {
			String queryStr = "SELECT COUNT(p.id) FROM " + c.getSimpleName()
					+ " p WHERE " + whereClause;
			Query query = generateQuery(queryStr, parameters, em);
			return (Long) query.getSingleResult();
		} catch (NoResultException e) {
			return -1L;
		} finally {
			em.close();
		}
	}

	/**
     * {@inheritDoc}
     */
	public List<?> getFieldValues(Class<? extends PersistentObject> c,
			String field, String whereClause, Map<String, Object> parameters) {
		String queryStr = createSelectFieldQuery(c, field, whereClause);
		EntityManager em = getEntityManager();
		Query query = generateQuery(queryStr, parameters, em);
		
		try {
			return query.getResultList();
		} finally {
			em.close();
		}
	}

}