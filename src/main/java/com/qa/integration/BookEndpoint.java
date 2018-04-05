package com.qa.integration;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.qa.service.business.BookService;

@Path("/book") //http://localhost:8080/finalbook/rest/book
public class BookEndpoint {

	@Inject
	private BookService service;

	@Path("/json") //http://localhost:8080/finalbook/rest/book/json
	@GET //Method is activated on GET requests
	@Produces({ "application/json" })
	public String getAllBook() {
		return service.getAllBooks();
	}

	@Path("/json") //http://localhost:8080/finalbook/rest/book/json
	@POST //Method is activated on POST requests
	@Produces({ "application/json" })
	public String addBook(String book) {
		return service.addBook(book);
	}

	@Path("/json/{id}") //http://localhost:8080/finalbook/rest/book/json/<book id to be updated>
	@PUT //Method is activated on PUT requests
	@Produces({ "application/json" })
	public String updateBook(@PathParam("id") Long id, String book) {
		return service.updateBook(id, book);
	}

	@Path("/json/{id}") //http://localhost:8080/finalbook/rest/book/json/<book id to be deleted>
	@DELETE //Method is activated on DELETE requests
	@Produces({ "application/json" })
	public String deleteBook(@PathParam("id") Long id) {
		return service.deleteBook(id);

	}

	public void setService(BookService service) {
		this.service = service;
	}

}