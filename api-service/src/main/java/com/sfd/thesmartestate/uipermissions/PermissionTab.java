package com.sfd.thesmartestate.uipermissions;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tb_ui_permission_tabs")
@Data
@SuppressFBWarnings("EI_EXPOSE_REP")

public class PermissionTab implements Serializable, Comparable<PermissionTab> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public PermissionTab build(final String path,
                               final String title,
                               int index,
                               String moduleName,
                               String icon,
                               String cssClass,
                               boolean groupTitle,
                               Set<PermissionTab> submenu,
                               boolean isTopMenu) {
        this.path = path;
        this.title = title;
        this.index = index;
        this.moduleName = moduleName;
        this.icon = icon;
        this.cssClass = cssClass;
        this.groupTitle = groupTitle;
        this.submenu = submenu;
        this.isTopMenu = isTopMenu;
        return this;
    }

    @Column(name = "ui_tab_index")
    private Integer index;
    @Column(name = "path")
    private String path;
    @Column(name = "title")
    private String title;
    @Column(name = "module_name")
    private String moduleName;
    @Column(name = "icon")
    private String icon;
    @Column(name = "group_title")
    private boolean groupTitle;
    @Column(name = "css_class")
    private String cssClass;
    @Column(name = "is_top_menu")
    private boolean isTopMenu;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "submenu_id", referencedColumnName = "id")
    )
    private Set<PermissionTab> submenu;

    @Override
    public int compareTo(PermissionTab o) {
        return o.path.compareTo(path);
    }
}
