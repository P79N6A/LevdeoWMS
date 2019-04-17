package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
public class Sysdiagrams implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer principalId;

    @TableId(value = "diagram_id", type = IdType.AUTO)
    private Integer diagramId;

    private Integer version;

    private byte[] definition;

    public Integer getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Integer principalId) {
        this.principalId = principalId;
    }
    public Integer getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(Integer diagramId) {
        this.diagramId = diagramId;
    }
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    public byte[] getDefinition() {
        return definition;
    }

    public void setDefinition(byte[] definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "Sysdiagrams{" +
        "principalId=" + principalId +
        ", diagramId=" + diagramId +
        ", version=" + version +
        ", definition=" + definition +
        "}";
    }
}
