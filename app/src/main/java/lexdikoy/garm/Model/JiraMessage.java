package lexdikoy.garm.Model;

import java.io.Serializable;

public class JiraMessage implements Serializable {

    private String issue_link;
    private String responsible;
    private String issue_title;
    private long timeMessage;

    public JiraMessage(String issue_link, String responsible, String issue_title, long timeMessage) {
        this.issue_link = issue_link;
        this.responsible = responsible;
        this.issue_title = issue_title;
        this.timeMessage = timeMessage;
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }

    public String getIssue_link() {
        return issue_link;
    }

    public void setIssue_link(String issue_link) {
        this.issue_link = issue_link;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getIssue_title() {
        return issue_title;
    }

    public void setIssue_title(String issue_title) {
        this.issue_title = issue_title;
    }
}
