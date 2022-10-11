package mx.conacyt.crip.ejemplos.openapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserPet.
 */
@Entity
@Table(name = "user_pet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserPet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "user_status", nullable = false)
    private Integer userStatus;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "userPet", "addresses" }, allowSetters = true)
    @OneToOne(mappedBy = "userPet")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserPet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserPet phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserStatus() {
        return this.userStatus;
    }

    public UserPet userStatus(Integer userStatus) {
        this.setUserStatus(userStatus);
        return this;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserPet user(User user) {
        this.setUser(user);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        if (this.customer != null) {
            this.customer.setUserPet(null);
        }
        if (customer != null) {
            customer.setUserPet(this);
        }
        this.customer = customer;
    }

    public UserPet customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPet)) {
            return false;
        }
        return id != null && id.equals(((UserPet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPet{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", userStatus=" + getUserStatus() +
            "}";
    }
}
