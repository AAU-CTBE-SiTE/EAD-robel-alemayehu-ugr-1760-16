package com.craft;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {


    public static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("studentPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ---------------------------------------------------------------- CREATE
    public static void createStudent(Student student) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(student);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            rollbackIfActive(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    // ------------------------------------------------------------------ READ
    public static Student getStudentById(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.find(Student.class, id);
        } finally {
            entityManager.close();
        }
    }

    // ---------------------------------------------------------------- UPDATE
    public static void updateStudentName(String newName, int id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, id);
            if (student == null) {
                System.out.println("No student with id " + id);
            } else {
                student.setName(newName); 
            }
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            rollbackIfActive(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public static void updateStudentNameAndTech(String newName, String newTech, int id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, id);
            if (student == null) {
                System.out.println("No student with id " + id);
            } else {
                student.setName(newName);
                student.setTech(newTech); 
            }
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            rollbackIfActive(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }

    // ---------------------------------------------------------------- DELETE
    public static void deleteStudentById(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, id);
            if (student == null) {
                System.out.println("No student with id " + id);
            } else {
                entityManager.remove(student);
            }
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            rollbackIfActive(entityManager);
            throw e;
        } finally {
            entityManager.close();
        }
    }


    public static void demonstrateContains(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            Student student = entityManager.find(Student.class, id);
            System.out.println("contains after find()   : " + entityManager.contains(student));
            entityManager.detach(student);
            System.out.println("contains after detach() : " + entityManager.contains(student));
        } finally {
            entityManager.close();
        }
    }

    private static void rollbackIfActive(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }


    public static void main(String[] args) {
        try {
            // CREATE
            Student student1 = new Student();
            student1.setName("Mohammed ali");
            student1.setTech("Python");
            createStudent(student1);
            int id = student1.getId(); 
            System.out.println("Created : " + student1);

            // READ
            System.out.println("Fetched : " + getStudentById(id));

            // UPDATE (name only)
            updateStudentName("Alemayehu", id);
            System.out.println("Renamed : " + getStudentById(id));

            // UPDATE (name + tech)
            updateStudentNameAndTech("Abebe", "Java", id);
            System.out.println("Updated : " + getStudentById(id));

            // contains()
            demonstrateContains(id);

            // DELETE
            deleteStudentById(id);
            System.out.println("After delete: " + getStudentById(id)); // null
        } finally {
            emf.close(); // release the connection pool so the JVM can exit
        }
    }
}
