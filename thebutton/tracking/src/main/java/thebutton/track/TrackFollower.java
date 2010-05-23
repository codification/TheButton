package thebutton.track;

/**
 * @author: aavisv
 * @created: 2010-05-21 4:59:46 PM
 */
public interface TrackFollower {
    TrackFollower NULL = new TrackFollower() {
        @Override
        public void add(Track o) {
            // empty
        }
    };

    void add(Track o);
}
