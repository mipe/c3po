package com.petpet.c3po.datamodel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostPersist;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllProperties", query = "SELECT p FROM Property p"),
    @NamedQuery(name = "getAllPropertyNames", query = "SELECT p.name FROM Property p") })
public class Property implements Serializable {

  private static final long serialVersionUID = -2404477153744982138L;

  private static final Logger LOG = LoggerFactory.getLogger(Property.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  private String name;

  @NotNull
  private String humanReadableName;

  private String description;

  @Enumerated(EnumType.STRING)
  private PropertyType type;

  // @OneToMany
  // private Set<Property> properties;

  public Property() {
    super();
    // this.properties = new HashSet<Property>();
    this.type = PropertyType.DEFAULT;
  }

  public Property(String name) {
    this();
    this.name = name;
    this.setHumanReadableName(name);
  }

  public Property(String name, String rName) {
    this();
    this.name = name;
    this.setHumanReadableName(rName);
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getHumanReadableName() {
    return humanReadableName;
  }

  public void setHumanReadableName(String humanReadableName) {
    this.humanReadableName = humanReadableName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  // public void setProperties(Set<Property> properties) {
  // this.properties = properties;
  // }
  //
  // public Set<Property> getProperties() {
  // return properties;
  // }

  public void setType(PropertyType type) {
    this.type = type;
  }

  public PropertyType getType() {
    return type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Property other = (Property) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (type != other.type)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  @PostPersist
  public void post() {
    LOG.trace("Found and stored new property " + this.getId() + " " + this.getName());
  }
}
