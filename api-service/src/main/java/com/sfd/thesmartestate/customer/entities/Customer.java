package com.sfd.thesmartestate.customer.entities;

import com.sfd.thesmartestate.users.entities.LoginDetails;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_customers")
@Data
public class Customer implements Serializable, Comparable<Customer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_email")
    private String email;

    @Column(name = "customer_phone")
    private String phone;

    @Column(name = "alternate_phone")
    private String alternatePhone;

    @OneToOne(cascade = CascadeType.PERSIST)
    private LoginDetails loginDetails;

    @Column(name = "customer_unique_id")
    private String customerUniqueId;

    @Column(name = "profile_image_thumb_path")
    private String profileImageThumbPath;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Override
    public int compareTo(Customer o) {
        return this.getPhone().compareTo(o.getPhone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!name.equals(customer.name)) return false;
        return phone.equals(customer.phone);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + phone.hashCode();
        return result;
    }
}
