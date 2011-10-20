/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

/**
 *
 * @author adam
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.gson.Gson;
import java.io.InputStream;
import java.util.List;


public class CatConv 
{
	  /*  private class Course
		{
    public List<String> major;
    public int number, units;
    public String description;
    public String name = "";
    public Set<Set<Course>> preRequisites;//list of lists for possiblities, outside or, inside and
    public Set<Set<Course>> coRequisites;// pre or co ^^
    
    public Course()
    {
    	major = new ArrayList<String>();
    	number = 0;
    	units = 0;
    	description = new String("");
    	preRequisites = new HashSet<Set<Course>>();
    	coRequisites = new HashSet<Set<Course>>();
    }
    
    public Course(String major, int number)
    {
    	this.major = new ArrayList<String>();
    	this.major.add(major);
    	this.number = number;
    }
		}*/
	public static void main(String[] args) throws IOException
	{
                InputStream is = CatConv.class.getResourceAsStream("Cat.txt");
                File f = new File("C:\\Users\\adam\\Documents\\NetBeansProjects\\trunk\\src\\edu\\calpoly\\razsoftware\\Cat.txt");
		Gson gson = new Gson();
		Scanner s = new Scanner(f);
		ArrayList<CatConvCourse> CatConvCourses = new ArrayList<CatConvCourse>();
		while(s.hasNextLine())
		{
    String tmp =s.nextLine();
    if(tmp.matches("[A-Za-z]{3,4} [0-9]{3} [^(]* .*[(].*"))
    {
    	CatConvCourse newCatConvCourse = new CatConvCourse();
    	int i=2;
    	String[] spl = tmp.split(" ");
    	newCatConvCourse.major.add(spl[0]);
    	newCatConvCourse.number = Integer.valueOf(spl[1]);
    	try
    	{
    		for(; !spl[i].startsWith("(");i++)
    		{
        newCatConvCourse.name += (spl[i]+" ");
    		}
    		newCatConvCourse.units = Integer.valueOf(spl[i].charAt(1)-'0');
    	}
    	catch(Exception E)
    	{
    		System.out.println(tmp);
    	}
    	
    	
    	
    	if(tmp.contains("(Also listed as"))
    	{
    		String[] alt = tmp.split("\\(Also listed as", 2);
    		newCatConvCourse.major.add(alt[1].split(" ")[1]);
    	}
    	
    	CatConvCourses.add(newCatConvCourse);
    }
    else if(CatConvCourses.size()>0)
    {
    	CatConvCourse c = CatConvCourses.remove(CatConvCourses.size()-1);
    	c.description += tmp;
    	CatConvCourses.add(c);
    }
		}
		File jsonout = new File("NewCat.json");
		FileWriter fwjson = new FileWriter(jsonout);
		for(CatConvCourse c : CatConvCourses)
		{
    if(c.description.startsWith("(Also listed as"))
    {
    	String[] spl = c.description.split("\\)", 2);
    	c.description = spl[1];
    	String[] spl2 = spl[0].split(" ");
    	c.major.add(spl2[3]);
    }
    
    
    if(c.description.contains("Prerequisite:"))
    {
    	String[] spl = c.description.split("Prerequisite:");
    	c.description = spl[0];
    	if(spl[1].contains("Corequisite"))
    	{
    		String[] spl2 = spl[1].split("Corequisite:");
    		c.preRequisites = parseReq(spl2[0]);
    		c.coRequisites = parseReq(spl2[1]);
    	}
    	else
    	{
    		c.preRequisites = parseReq(spl[1]);
    	}
    }
    
    
    if(c.description.contains("prerequisite:"))
    {
    	String[] spl = c.description.split("prerequisite:");
    	c.description = spl[0];
    	if(spl[1].contains("corequisite"))
    	{
    		String[] spl2 = spl[1].split("corequisite:");
    		c.preRequisites = parseReq(spl2[0]);
    		c.coRequisites = parseReq(spl2[1]);
    	}
    	else
    	{
    		c.preRequisites = parseReq(spl[1]);
    	}
    }
    fwjson.append(gson.toJson(c) + "\n");
		}
		fwjson.close();
		
	}
	
	public static Set<Set<CatConvCourse>> parseReq(String in)
	{
		Pattern CatConvCoursePattern = Pattern.compile("[A-Z]{3,4} [0-9]{3}");
		HashSet<Set<CatConvCourse>> req = new HashSet<Set<CatConvCourse>>();
		String[] stage1 = in.split("and|,|;");
		
		
		for(String s : stage1)
		{
    HashSet<CatConvCourse> out = new HashSet<CatConvCourse>();
    Matcher m = CatConvCoursePattern.matcher(s);
    while(m.find())
    {
    	out.add(new CatConvCourse(m.group().split(" ")[0], Integer.valueOf(m.group().split(" ")[1])));
    }
    if(out.size() != 0)
    	req.add(out);
		}
		if(req.size()==0)
                    return null;
		else
		{
                    HashSet<Set<CatConvCourse>> permutations = new HashSet<Set<CatConvCourse>>();
                    for(Set<CatConvCourse> s : req)
                    {
                        if(permutations.isEmpty())
                        {
                                for(CatConvCourse c : s)
                                {
                        HashSet<CatConvCourse> newitem = new HashSet<CatConvCourse>();
                        newitem.add(c);
                        permutations.add(newitem);
                                }
                        }
                        else
                        {
                                HashSet<Set<CatConvCourse>> newPerm = new HashSet<Set<CatConvCourse>>();
                                for(Set<CatConvCourse> s2 : permutations)
                                {
                        for(CatConvCourse c : s)
                        {
                                HashSet<CatConvCourse> dupeset = new HashSet<CatConvCourse>(s2);
                                dupeset.add(c);
                                newPerm.add(dupeset);
                        }
                                }
                                permutations = new HashSet<Set<CatConvCourse>>(newPerm);

                        }

                    }
                    req = permutations;
                }
                return req;
	}
	
}

