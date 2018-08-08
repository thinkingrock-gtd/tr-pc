package tr.model.util.delegation;

import java.util.Date;
import tr.model.util.delegation.DelegationData.Type;

/**
 * Builder of DelegationData objects.
 *
 * @author Jeremy Moore
 */
final class DelegationDataBuilder {

    private final Type type;
    private Integer id;
    private String reply;
    private String success;
    private String time;
    private String energy;    
    private String priority;
    private String topic;
    private Date start;
    private Date due;
    private Date done;

    DelegationDataBuilder(Type type) {
        this.type = type;
    }

    DelegationDataBuilder id(int id) {
        this.id = id;
        return this;
    }

    DelegationDataBuilder reply(String s) {
        reply = s;
        return this;
    }

    DelegationDataBuilder success(String s) {
        success = s;
        return this;
    }

    DelegationDataBuilder time(String s) {
        time = s;
        return this;
    }

    DelegationDataBuilder energy(String s) {
        energy = s;
        return this;
    }

    DelegationDataBuilder priority(String s) {
        priority = s;
        return this;
    }

    DelegationDataBuilder topic(String s) {
        topic = s;
        return this;
    }

    DelegationDataBuilder start(Date date) {
        start = date;
        return this;
    }

    DelegationDataBuilder start(long ms) {
        start = ms > 0 ? new Date(ms) : null;
        return this;
    }

    DelegationDataBuilder due(Date date) {
        this.due = date;
        return this;
    }

    DelegationDataBuilder due(long ms) {
        due = ms > 0 ? new Date(ms) : null;
        return this;
    }

    DelegationDataBuilder done(Date date) {
        this.done = date;
        return this;
    }

    DelegationDataBuilder done(long ms) {
        done = ms > 0 ? new Date(ms) : null;
        return this;
    }

    DelegationData make() {
        return new DelegationData(type, id, reply, success, time, energy, priority, topic, start, due, done);
    }

}
