package com.example.librarycrud;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    // ---------- CRUD ----------

    // Create
    @PostMapping
    public Book create(@RequestBody Book book) {
        return repository.save(book);
    }

    // Read all
    @GetMapping
    public List<Book> getAll() {
        return repository.findAll();
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Integer id, @RequestBody Book updated) {
        return repository.findById(id).map(book -> {
            book.setTitle(updated.getTitle());
            book.setAuthor(updated.getAuthor());
            book.setGenre(updated.getGenre());
            book.setPublishedYear(updated.getPublishedYear());
            book.setCopies(updated.getCopies());
            return ResponseEntity.ok(repository.save(book));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Search and filter ----------

    @GetMapping("/author/{author}")
    public List<Book> getByAuthor(@PathVariable String author) {
        return repository.findByAuthorIgnoreCase(author);
    }

    @GetMapping("/genre/{genre}")
    public List<Book> getByGenre(@PathVariable String genre) {
        return repository.findByGenreIgnoreCase(genre);
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    @GetMapping("/available")
    public List<Book> available() {
        return repository.findByCopiesGreaterThan(0);
    }

    // ---------- Borrow and return ----------

    @PostMapping("/{id}/borrow")
    public ResponseEntity<?> borrow(@PathVariable Integer id) {
        return repository.findById(id).map(book -> {
            if (book.getCopies() == null || book.getCopies() <= 0) {
                return ResponseEntity.badRequest().body((Object) "No copies available to borrow.");
            }
            book.setCopies(book.getCopies() - 1);
            return ResponseEntity.ok((Object) repository.save(book));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Book> giveBack(@PathVariable Integer id) {
        return repository.findById(id).map(book -> {
            book.setCopies(book.getCopies() == null ? 1 : book.getCopies() + 1);
            return ResponseEntity.ok(repository.save(book));
        }).orElse(ResponseEntity.notFound().build());
    }
}