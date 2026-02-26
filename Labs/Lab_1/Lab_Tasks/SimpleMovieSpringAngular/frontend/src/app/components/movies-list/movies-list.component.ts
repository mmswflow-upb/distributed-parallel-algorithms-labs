import { Component, OnInit } from '@angular/core';
import { Movie } from 'src/app/models/movie.model';
import { MovieService } from 'src/app/services/movie.service';

@Component({
  selector: 'app-movies-list',
  templateUrl: './movies-list.component.html',
  styleUrls: ['./movies-list.component.css']
})
export class MoviesListComponent implements OnInit {

  movies?: Movie[];
  currentMovie: Movie = {};
  currentIndex = -1;
  title = '';

  constructor(private movieService: MovieService) { }

  ngOnInit(): void {
    this.retrieveMovies();
  }

  retrieveMovies(): void {
    this.movieService.getAll()
      .subscribe({
        next: (data) => {
          this.movies = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  refreshList(): void {
    this.retrieveMovies();
    this.currentMovie = {};
    this.currentIndex = -1;
  }

  setActiveMovie(movie: Movie, index: number): void {
    this.currentMovie = movie;
    this.currentIndex = index;
  }

  removeAllMovies(): void {
    this.movieService.deleteAll()
      .subscribe({
        next: (res) => {
          console.log(res);
          this.refreshList();
        },
        error: (e) => console.error(e)
      });
  }

  searchTitle(): void {
    this.currentMovie = {};
    this.currentIndex = -1;

    this.movieService.findByTitle(this.title)
      .subscribe({
        next: (data) => {
          this.movies = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
}
