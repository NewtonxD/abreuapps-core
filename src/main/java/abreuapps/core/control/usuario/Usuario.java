package abreuapps.core.control.usuario;

import abreuapps.core.control.general.Persona;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * Esta entidad representa los usuarios que manejan el sistema.
 *
 * @author Carlos Abreu Pérez
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr",schema = "public")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@UsuarioId")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "usr")
    private String username;

    @Column(name = "mail")
    private String correo;

    @Column(name = "pwd")
    @JsonIgnore
    private String password;

    @Column(name = "act")
    private boolean activo;

    @Column(name="hecho_por_id")
    private Integer hecho_por;
    
    @Column(name= "mde_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_registro;
    
    @Column(name="actualizado_por_id")
    private Integer actualizado_por;
    
    @Column(name= "upd_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;
    
    @Column(name = "pwd_chg")
    private boolean cambiarPassword;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id",nullable = false) // Nombre de la columna de clave foránea
    private Persona persona;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return activo;
    }

    @Override
    public boolean isAccountNonLocked() {
        return activo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return ! cambiarPassword;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
