package com.pwccn.esg.model;

import javax.persistence.*;

@Entity
@Table(name = "topic")
public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "domain_id")
    private DomainEntity domain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DomainEntity getDomain() {
        return domain;
    }

    public void setDomain(DomainEntity domain) {
        this.domain = domain;
    }
}
