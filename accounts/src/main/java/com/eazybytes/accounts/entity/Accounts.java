package com.eazybytes.accounts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Accounts extends BaseEntity{

    @Column(name="customer_id")
    private Long customerId;
    @Id
    private Long accountNumber;
    private String accountType;
    private String branchAddress;

    @Column(name = "communication_sw")
    private Boolean communicationSw;


}
