package com.realappraiser.gharvalue.ticketRaiseSystem.model;

public class GetRaisedTickets {

    private String fromdate;

    private String todate;

    private String fillterby;

    private String ticketId;

    private String status;

    private int roleId;

    private int EmpId;

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public void setFillterby(String fillterby) {
        this.fillterby = fillterby;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setEmpId(int empId) {
        EmpId = empId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromdate() {
        return fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public String getFillterby() {
        return fillterby;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getStatus() {
        return status;
    }

    public int getRoleId() {
        return roleId;
    }

    public int getEmpId() {
        return EmpId;
    }
}
