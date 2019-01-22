package com.pwccn.esg.model;

import com.pwccn.esg.dto.DomainDTO;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "domain")
public class DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TopicEntity> topics;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DomainEntity(){

    }
    public DomainEntity(DomainDTO domainDTO){
        setName(domainDTO.getName());
        setTopics(domainDTO.getTopics());
    }

    public List<TopicEntity> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicEntity> topics) {
        this.topics = topics;
    }
}
