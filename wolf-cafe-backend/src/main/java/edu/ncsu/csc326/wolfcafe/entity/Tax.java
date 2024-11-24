package edu.ncsu.csc326.wolfcafe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tax entity for storing tax rate information in the system.
 *
 * @author Narim Lee
 */
@Entity
@Table ( name = "tax" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tax {
    
    /** The unique id of the tax */
    @Id
    private Long   id;

    /** The actually value for tax rate for the system */
    @Column ( nullable = false )
    private double taxRate;
}
