package cz.cvut.fel.recordManagerStatisticsServer.repository.model;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.net.URI;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
@ToString
public abstract class AbstractEntity implements Serializable {

    @Id
    private URI uri;

}