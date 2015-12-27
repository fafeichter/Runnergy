package com.ffeichta.runnergy;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.Coordinate;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;
import java.util.Date;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    DBAccessHelper db = null;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        db = DBAccessHelper.getInstance(getContext());
    }

    /**
     * Test objects
     */
    public void testDBAccesHelperObject() {
        assertNotNull(db);
    }

    /**
     * Test get methods
     */
    public void testCountTracks() {
        assertEquals(2, db.getTracks().size());
    }

    public void testOrderOfGetTracksMethod() {
        Track t = db.getTracks().get(0);
        assertEquals("Ahornach-Knutten", t.getName());
    }

    public void testCountOfActivitiesOfTrack() {
        Track t = db.getTracks().get(1);
        assertEquals(1, db.getActivities(t).size());
    }

    public void testTypeOfActivityOfTrack() {
        Activity a = db.getTracks().get(0).getActivities().get(0);
        assertEquals(Activity.Type.RUNNING.toString(), a.getType().toString());
    }

    public void testCountOfCoordinatesOfActivity() {
        Activity a = db.getTracks().get(1).getActivities().get(0);
        ArrayList<Coordinate> c = db.getCoordinates(a);
        assertEquals(4, c.size());
    }

    public void testGetUnknownActivity() {
        assertEquals(null, db.getActivity(100));
    }

    public void testGetUnknownCoordinate() {
        assertEquals(null, db.getCoordinate(100));
    }

    public void testClosestCoordinate() {
        assertEquals(1, db.getIDOfClosestCoordinateInActivity(11.354912, 46.499562, 1));
    }


    /**
     * Test insert methods
     */
    public void testInsertTrack() {
        Track t = new Track();
        t.setName("Bruneck-Bozen");
        assertEquals(0, db.insertTrack(t));
        db.deleteTrack(t);
    }

    public void testInsertTrackWhichAlreadyExists() {
        Track t = new Track();
        t.setName("Ahornach-Knutten");
        db.insertTrack(t);
        assertEquals(Track.NAME_ALREADY_EXISTS, (int) t.getError().get("name"));
    }

    public void testInsertActivityWithNoTrack() {
        Activity a1 = new Activity();
        assertEquals(-1, db.insertActivity(a1));
    }

    public void testInsertActivityWithErrorInCoordinate() {
        Activity a = new Activity();
        a.setType(Activity.Type.RUNNING);
        a.setDate(new Date().getTime());
        a.setDuration(1259);
        a.setTrack(db.getTracks().get(0));
        Coordinate c = new Coordinate();
        c.setLongitude(11.354912);
        c.setLatitude(59.00089);
        c.setStart(true);
        c.setEnd(false);
        c.setTimeFromStart(0);
        c.setDistanceFromPrevious(0);
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(c);
        // No activity
        //c.setActivity(a);
        a.setCoordinates(coordinates);
        assertEquals(-1, db.insertActivity(a));
        db.deleteActivity(a);
    }

    public void testInsertActivity() {
        Activity a = new Activity();
        a.setType(Activity.Type.RUNNING);
        a.setDate(new Date().getTime());
        a.setDuration(1259);
        a.setTrack(db.getTracks().get(0));
        Coordinate c = new Coordinate();
        c.setLongitude(11.0003);
        c.setLatitude(59.00089);
        c.setStart(true);
        c.setEnd(false);
        c.setTimeFromStart(0);
        c.setDistanceFromPrevious(0);
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(c);
        c.setActivity(a);
        a.setCoordinates(coordinates);
        assertEquals(0, db.insertActivity(a));
    }

    /**
     * Test update methods
     */
    public void testUpdateTrack() {
        Track t = db.getTracks().get(0);
        t.setName("Nals-Mals");
        db.updateTrack(t);
        assertEquals("Nals-Mals", db.getTracks().get(0).getName());
    }

    /**
     * Test delete methods
     */
    public void testDeleteActivity() {
        Activity a = db.getTracks().get(0).getActivities().get(0);
        assertEquals(0, db.deleteActivity(a));
        assertEquals(null, db.getCoordinates(a));
    }

    public void testDeleteTrack() {
        Track t = db.getTracks().get(0);
        assertEquals(0, db.deleteTrack(t));
    }
}