package org.example.model;


import org.example.config.FactoryConfiguration;
import org.example.entity.Song;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Random;

public class SongModel {
    public static boolean saveSong(Song song){
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(song);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }


    public static boolean deleteSong(Song song) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(song);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public static List<Song> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        List<Song> songs = session.createQuery("FROM Song", Song.class).list();
        transaction.commit();
        session.close();
        return songs;
    }
    public static Song getNextSong() {
        List<Song> allSongs = getAll();

        if (!allSongs.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(allSongs.size());
            return allSongs.get(randomIndex);
        } else {
            return null;
        }
    }
}

