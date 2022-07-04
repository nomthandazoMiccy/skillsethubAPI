package com.skillsethub.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.skillsethub.object.Skill;
import com.skillsethub.object.User;

@Path("/search")
public class SearchController {

	@GET
	 @Path("/{item}")
	 @Produces(MediaType.APPLICATION_JSON)
	 public List<Skill> searchItems(@PathParam("item") String item){
		 
		 List<Skill> skills = new ArrayList<Skill>();
		 
		 try {
	   	  Class.forName("com.mysql.jdbc.Driver");
	   	      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
		      
		      String query = "SELECT * FROM users u INNER JOIN skills s ON u.UserId = s.UserId AND ProgrammingLanguages LIKE ? OR JobTitle LIKE ?";
		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		  
		      preparedStmt.setString(1,"%"+ item + "%");
		      preparedStmt.setString(2,"%"+ item + "%");

			  ResultSet resultSet = preparedStmt.executeQuery();
		      while(resultSet.next()) {
		    	 
		    	  Skill skill = new Skill(resultSet.getString("ProgrammingLanguages"),
		    			        resultSet.getString("SkillsDesc"),resultSet.getInt("UserId"),resultSet.getString("Experience"),
		    			        resultSet.getString("JobTitle"));
		    	  User user = new User(resultSet.getInt("UserId"),resultSet.getString("FirstName"), 
					             resultSet.getString("LastName"),resultSet.getString("Email"),
					             resultSet.getString("Password"),resultSet.getString("Address"),resultSet.getInt("RoleId"),resultSet.getString("Contact"));
		 
				 skill.setUser(user);
				 skills.add(skill);
		      }
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 
		 return skills;
	 }
	 
	 @GET
	 @Path("/all")
	 @Produces(MediaType.APPLICATION_JSON)
	 public List<Skill> getSkills(){
		 
		 List<Skill> skills = new ArrayList<Skill>();
		 try {
		   	  Class.forName("com.mysql.jdbc.Driver");
		   	      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
		   	      Statement st=conn.createStatement();
			      String query = "SELECT * FROM skills s inner join users u on s.UserId = u.UserId";
			      ResultSet resultSet = st.executeQuery(query);
			      
			      while(resultSet.next()){ 
			    	  
			    	  Skill skill = new Skill(resultSet.getString("ProgrammingLanguages"), resultSet.getString("SkillsDesc"),resultSet.getInt("UserId"),resultSet.getString("Experience"),resultSet.getString("JobTitle"));
			    	 
			    	  User user = new User(resultSet.getInt("UserId"),resultSet.getString("FirstName"), 
					             resultSet.getString("LastName"),resultSet.getString("Email"),
					             resultSet.getString("Password"),resultSet.getString("Address"),resultSet.getInt("RoleId"),resultSet.getString("Contact"));
			    	  
			    	  skill.setUser(user);
			    	  skills.add(skill);
			      }
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		 
		 return skills;
	 }

}
