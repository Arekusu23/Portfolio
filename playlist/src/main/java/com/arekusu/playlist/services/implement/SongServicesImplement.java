	package com.arekusu.playlist.services.implement;

    import com.arekusu.playlist.models.dtos.SongDTO;
    import com.arekusu.playlist.models.entities.Song;
    import com.arekusu.playlist.repositories.SongRepository;
    import com.arekusu.playlist.services.SongService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.UUID;
	


	@Service
	public class SongServicesImplement implements SongService {

	    private final SongRepository songRepository;

	    @Autowired
	    public SongServicesImplement(SongRepository songRepository) {
	        this.songRepository = songRepository;
	    }

	    @Override
	    public List<Song> getAllSongs() {
	        return songRepository.findAll();
	    }

	    @Override
	    public List<Song> searchSongsByTitle(String title) {
	        return songRepository.findByTitleContainingIgnoreCase(title);
	    }

	    @Override
	    public boolean deleteSongByTitle(String title) {
	        List<Song> songs = songRepository.findByTitle(title);
	        if (!songs.isEmpty()) {
	            songRepository.deleteAll(songs);
	            return true;
	        } else {
	            return false;
	        }
	    }

	    @Override
	    public Song createSong(SongDTO songDTO) {
	        if (songDTO.getTitle() == null || songDTO.getTitle().isEmpty()) {
	            throw new IllegalArgumentException("El título de la canción es requerido.");
	        }
	        if (songDTO.getTotalDurationInSeconds() <= 0) {
	            throw new IllegalArgumentException("La duración de la canción debe ser mayor a 0.");
	        }

	        Song song = new Song(songDTO.getTitle(), songDTO.getTotalDurationInSeconds());
	        return songRepository.save(song);
	    }

	    @Override
	    public Song createSong(Song song) {
	        if (song.getTitle() == null || song.getTitle().isEmpty()) {
	            throw new IllegalArgumentException("El título de la canción es requerido.");
	        }
	        if (song.getDuration() <= 0) {
	            throw new IllegalArgumentException("La duración de la canción debe ser mayor a 0.");
	        }
	        return songRepository.save(song);
	    }

		@Override
		public Song searchSongByCode(String code) {	
			try {
				UUID codeParsed = UUID.fromString(code);
			return songRepository.findOneByCode(codeParsed);
		}catch(IllegalArgumentException e){
			throw new IllegalArgumentException("Ha ocurrido un error al parsear el code");
		}
			
		}
	}
