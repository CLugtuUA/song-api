package com.lugtu.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lugtu/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    // 1. GET all songs
    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    // 2. POST to add a new song
    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return songRepository.save(song);
    }

    // 3. GET a specific song by ID
    @GetMapping("/{id}")
    public Song getSongById(@PathVariable Long id) {
        Optional<Song> song = songRepository.findById(id);
        return song.orElse(null);
    }

    // 4. PUT to update an existing song by ID
    @PutMapping("/{id}")
    public Song updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        return songRepository.findById(id).map(song -> {
            song.setTitle(songDetails.getTitle());
            song.setArtist(songDetails.getArtist());
            song.setAlbum(songDetails.getAlbum());
            song.setGenre(songDetails.getGenre());
            song.setUrl(songDetails.getUrl());
            return songRepository.save(song);
        }).orElse(null);
    }

    // 5. DELETE a song by ID
    @DeleteMapping("/{id}")
    public String deleteSong(@PathVariable Long id) {
        songRepository.deleteById(id);
        // The Postman test explicitly checks for this exact string response:
        return "Song with ID " + id + " deleted.";
    }

    // 6. GET to search for a song across multiple fields
    @GetMapping("/search/{query}")
    public List<Song> searchSongs(@PathVariable String query) {
        return songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrGenreContainingIgnoreCase(
                query, query, query, query
        );
    }
}