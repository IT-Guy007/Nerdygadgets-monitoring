package nerdygadgets.Monitoring;

class Project {
    private int ProjectID;
    private String name;
    private double wanted_availability;

    Project(int projectID,String name,double wanted_availability) {
        this.ProjectID = projectID;
        this.name = name;
        this.wanted_availability = wanted_availability;
    }

    @Override
    public String toString() {
        return "ProjectID = " + ProjectID +
                ", name='" + name + '\'' +
                ", wanted_availability=" + wanted_availability +
                '}';
    }

    public int getProjectID() {
        return ProjectID;
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWanted_availability() {
        return wanted_availability;
    }

    public void setWanted_availability(double wanted_availability) {
        this.wanted_availability = wanted_availability;
    }
}