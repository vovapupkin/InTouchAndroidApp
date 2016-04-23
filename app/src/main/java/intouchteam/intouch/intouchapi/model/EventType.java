package intouchteam.intouch.intouchapi.model;


public class EventType {
    private Long id;
    private String typeName;

    public EventType() {}

    public String getTypeName() {
        return typeName;
    }

    public Long getId() {
        return id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
