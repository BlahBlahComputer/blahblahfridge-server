package sogang.capstone.blahblahfridge.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import org.hibernate.annotations.UpdateTimestamp;

public class AbstractTimestamp {

    @Column(name = "created_at")
    @UpdateTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}