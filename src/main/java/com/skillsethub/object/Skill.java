package com.skillsethub.object;

public class Skill {
   
	private String skillDesc;
	private String programmingLanguage;
	private String jobTitle;
	private String experience;
	private int userId;
	private User user;
	
	public Skill() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Skill(String programmingLanguage,String skillDesc,int userId ,String experience, String jobTitle) {
		
		this.skillDesc = skillDesc;
		this.programmingLanguage = programmingLanguage;
		this.jobTitle = jobTitle;
		this.experience = experience;
		this.userId = userId;
	}

	public String getSkillDesc() {
		return skillDesc;
	}

	public void setSkillDesc(String skillDesc) {
		this.skillDesc = skillDesc;
	}

	public String getProgrammingLanguage() {
		return programmingLanguage;
	}

	public void setProgrammingLanguage(String programmingLanguage) {
		this.programmingLanguage = programmingLanguage;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
