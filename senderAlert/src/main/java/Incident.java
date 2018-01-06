
/**
 * A Incident.
 */
public class Incident {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String kind;

    private String location;


    private Boolean lifeDanger;

    private Notifier notifier;

    private Boolean isNotifierVictim;

    private Boolean sufferer;

    private Integer howManyVictims;

    private String otherCircumstances;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getLifeDanger() {
        return lifeDanger;
    }

    public void setLifeDanger(Boolean lifeDanger) {
        this.lifeDanger = lifeDanger;
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    public Boolean getIsNotifierVictim() {
        return isNotifierVictim;
    }

    public void setIsNotifierVictim(Boolean isNotifierVictim) {
        this.isNotifierVictim = isNotifierVictim;
    }

    public Boolean getSufferer() {
        return sufferer;
    }

    public void setSufferer(Boolean sufferer) {
        this.sufferer = sufferer;
    }

    public Integer getHowManyVictims() {
        return howManyVictims;
    }

    public void setHowManyVictims(Integer howManyVictims) {
        this.howManyVictims = howManyVictims;
    }

    public String getOtherCircumstances() {
        return otherCircumstances;
    }

    public void setOtherCircumstances(String otherCircumstances) {
        this.otherCircumstances = otherCircumstances;
    }
}
