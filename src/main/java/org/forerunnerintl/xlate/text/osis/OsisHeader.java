package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class OsisHeader {
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OsisRevisionDescription> revisionDesc;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OsisWork> work;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<WorkPrefix> workPrefix;

    public List<OsisRevisionDescription> getRevisionDesc() {
        return revisionDesc;
    }

    public void setRevisionDesc(List<OsisRevisionDescription> revisionDesc) {
        this.revisionDesc = revisionDesc;
    }

    public List<OsisWork> getWork() {
        return work;
    }

    public void setWork(List<OsisWork> work) {
        this.work = work;
    }

    public List<WorkPrefix> getWorkPrefix() {
        return workPrefix;
    }

    public void setWorkPrefix(List<WorkPrefix> workPrefix) {
        this.workPrefix = workPrefix;
    }
}
