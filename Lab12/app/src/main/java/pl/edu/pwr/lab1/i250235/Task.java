package pl.edu.pwr.lab1.i250235;

import java.util.Date;

public class Task
{
    private String title;
    private String description;
    private String type;
    private String icon;
    private Date due_date;
    private boolean status;


    public Task(String title,String description,String type,String icon,Date dueDate, boolean status)
    {
        this.title=title;
        this.description=description;
        this.type=type;
        this.icon=icon;
        this.due_date = dueDate;
        this.status = status;
    }

    public String getTitle(){
        return  title;
    }
    public Date getDate(){
        return  due_date;
    }

}
