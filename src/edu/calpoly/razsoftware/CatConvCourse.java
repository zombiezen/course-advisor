/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

/**
 *
 * @author adam
 */
import java.util.*;

import com.google.gson.Gson;

public class CatConvCourse {
   public List<String> major;
   public int number, units;
   public String description;
   public String name = "";
   public Set<Set<CatConvCourse>> preRequisites;//list of lists for possiblities, outside or, inside and
   public Set<Set<CatConvCourse>> coRequisites;// pre or co ^^
   
   public CatConvCourse()
   {
	   major = new ArrayList<String>();
	   number = 0;
	   units = 0;
	   description = new String("");
	   preRequisites = new HashSet<Set<CatConvCourse>>();
	   coRequisites = new HashSet<Set<CatConvCourse>>();
   }
   
   public boolean equals(Object obj)
   {
	   if(this.getClass() != obj.getClass())  //is same class
		   return false;
	   boolean eq = false;
	   for(String s : major)					//has common major name
	   {
		   for(String s2 : ((CatConvCourse)obj).major)
		   {
			   eq |= (s.equalsIgnoreCase(s2));
		   }
	   }
	   eq &= (number == ((CatConvCourse)obj).number);	//has same course number
	   return eq;	   
   }
   
   public CatConvCourse(String major, int number)
   {
	   this.major = new ArrayList<String>();
	   this.major.add(major);
	   this.number = number;
   }
   
   public String ToBasicString()
   {  
	   StringBuilder sb = new StringBuilder("");
	   for(String s : major)
	   {
		   sb.append(s + "/");
	   }
	   sb.deleteCharAt(sb.length()-1);
	   sb.append(" " + Integer.toString(number));
	   return sb.toString();	   
   }
   
   public String ToString()
   {
	   StringBuilder sb = new StringBuilder("");
	   for(String s : major)
	   {
		   sb.append(s + "/");
	   }
	   sb.deleteCharAt(sb.length()-1);
	   sb.append(" \t ").append(Integer.toString(number));
	   sb.append(" \t " + Integer.toString(units));
	   sb.append(" \t " + name);
	   sb.append(" \t \"" + description + "\"");
	   sb.append(" \t, [");
	   if(preRequisites != null && preRequisites.size()!=0)
	   {
		   for(Set<CatConvCourse> s : preRequisites)
		   {
			   CatConvCourse[] CatConvCourses = new CatConvCourse[s.size()];
			   s.toArray(CatConvCourses);
			   sb.append("[");
			   for(CatConvCourse c : CatConvCourses)
			   {
				   sb.append(c.ToBasicString() + " ");
			   }
			   sb.append("]");
		   }
	   }
	   sb.append("]");
	   sb.append(" \t [");
	   if(coRequisites.size()!=0)
	   {
		   for(Set<CatConvCourse> s : coRequisites)
		   {
			   CatConvCourse[] CatConvCourses = new CatConvCourse[s.size()];
			   s.toArray(CatConvCourses);
			   sb.append("[");
			   for(CatConvCourse c : CatConvCourses)
			   {
				   sb.append(c.ToBasicString()).append(" ");
			   }
			   sb.append("]");
		   }
	   }
	   sb.append("]");
	   return sb.toString();
   }
   
   /*---------'get' methods---------*/
   public int getNumber()
   {
	   return number;
   }
   
   public String getName()
   {
	   return name;
   }
   public int getUnits()
   {
	   return units;
   }
   
   public String getDesc()
   {
	   return description;
   }
    
   /*---------'set' methods---------*/
   public void setNumber(int Number)
   {
	   number = Number;
   }
   
   public void setUnits(int Units)
   {
	   units = Units;
   }
   
   public void setDesc(String Description)
   {
	   description = Description;
   }
   
   public void setName(String Name)
   {
	   name = Name;
   }
   
   public void setMajor(Collection<String> Major)
   {
	   major = new ArrayList<String>(Major);
   }
   
   public CatConvCourse(List<String> major, int number, int units, String description) {
  
   }
   
   boolean preRecsMet(List<CatConvCourse> taken) {
	return false;
   }
}
