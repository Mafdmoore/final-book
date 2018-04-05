package com.qa.service.repository;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.Collection;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.qa.domain.Book;
import com.qa.util.JSONUtil;

@Transactional(SUPPORTS) //Sets transactional tag for the class
@Default //Sets this class as the default implementation of the 'BookRepository' interface
public class BookDBRepository implements BookRepository {

	@PersistenceContext(unitName = "primary")
	private EntityManager manager; //Request entity manager (the object that talks to the database), configured in persistence.xml

	@Inject //Tells the bean container to take over the lifecycle management of this object
	private JSONUtil util; //Provides with new object instance

	@Override
	public String getAllBooks() {
		Query query = manager.createQuery("Select a FROM Book a"); //Query object
		Collection<Book> book = (Collection<Book>) query.getResultList(); //Save result of query to variable
		return util.getJSONForObject(book); //Return variable as JSON objects
	}

	@Override
	@Transactional(REQUIRED) //Overrides @Transactional(SUPPORTS) tag
	public String createBook(String book) {
		Book aBook = util.getObjectForJSON(book, Book.class); //Get JSON object of the book to insert into database
		manager.persist(aBook); //Insert into database
		return "{\"message\": \"book has been sucessfully added\"}"; //Return success message
	}

	@Override
	@Transactional(REQUIRED) //Overrides @Transactional(SUPPORTS) tag
	public String updateBook(Long id, String bookToUpdate) {
		Book updatedBook = util.getObjectForJSON(bookToUpdate, Book.class); //Get JSON object of the book to update with
		Book bookFromDB = findBook(id); //Find the book we want to update
		if (bookToUpdate != null) { //If book exists
			bookFromDB = updatedBook; //Overwrite local object
			manager.merge(bookFromDB); //Insert local object into database
		}
		return "{\"message\": \"book sucessfully updated\"}"; //Return success message
	}

	@Override
	@Transactional(REQUIRED)
	public String deleteBook(Long id) {
		Book bookInDB = findBook(id);
		if (bookInDB != null) {
			manager.remove(bookInDB);
		}
		return "{\"message\": \"book sucessfully deleted\"}"; //Return success message
	}

	private Book findBook(Long id) {
		return manager.find(Book.class, id);
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	public void setUtil(JSONUtil util) {
		this.util = util;
	}

}