package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import org.codehaus.jackson.map.*;
import java.io.IOException;

import java.util.*;
import models.*;

public class Application extends Controller {

	public static void index(String json, String className) {
		if (className == null) className = "JSON2Apex";
		render(json, className);
	}
	
 	public static void makeApex(@Required String json, @Required String className) {
	 	if (Validation.hasErrors()) {
	        flash.error("Oops, please enter your json and className!");
	        index(json, className);
	    }
		try {
			ObjectMapper m = new ObjectMapper();
			Object o = m.readValue(json, Object.class);
			TypeFactory factory = new TypeFactory();
			ApexType root = factory.typeOfObject("Root", o);
			List<ApexClass> classes = factory.getClasses();
			request.format = "txt";
			render(className, json, root, classes);
			
		} catch (IOException ex) {
			flash.error("sorry, unable to parse your json: " + ex.getMessage());
			index(json, className);
		}
    }
}