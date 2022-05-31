package nerdygadgets.Monitoring;

class Project {
    int ProjectID;
    String name;
    int wanted_availability;

    Project(int projectID,String name,int wanted_availability) {
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
}