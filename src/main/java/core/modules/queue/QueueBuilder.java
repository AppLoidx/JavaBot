package core.modules.queue;

/**
 * @author Arthur Kupriyanov
 */
public class QueueBuilder {
    public static SimpleQueue createSimpleQueue(QueueObject qo){
        SimpleQueue sq = new SimpleQueue(qo.getName());
        qo.getQueue().values().forEach(sq::addPerson);
        sq.setFreeId(qo.getFreeId());
        sq.setDescription(qo.getDescription());
        sq.setCurrentPlace(qo.getCurrentPlace());
        sq.setStat(qo.getStat());
        return sq;
    }
    public static ShuffleQueue createShuffleQueue(QueueObject qo){
        ShuffleQueue sq = new ShuffleQueue(qo.getName());
        qo.getQueue().values().forEach(sq::addPerson);
        sq.setFreeId(qo.getFreeId());
        sq.setDescription(qo.getDescription());
        sq.setCurrentPlace(qo.getCurrentPlace());
        sq.setStat(qo.getStat());
        return sq;
    }
}
