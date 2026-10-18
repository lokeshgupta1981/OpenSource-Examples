package com.howtodoinjava.vaadincrud.book;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private final BookRepository bookRepository;

    private final Grid<Book> grid = new Grid<>(Book.class);
    private final TextField filterText = new TextField();
    private final BookForm form = new BookForm();

    public MainView(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        setSizeFull();

        configureGrid();
        configureForm();

        add(buildToolbar(), buildContent());

        updateGrid();
    }

    private HorizontalLayout buildToolbar() {
        filterText.setPlaceholder("Filter by title...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateGrid());

        Button addBookButton = new Button("Add book");
        addBookButton.addClickListener(e -> addBook());

        return new HorizontalLayout(filterText, addBookButton);
    }

    private HorizontalLayout buildContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("title", "author", "available");
        grid.asSingleSelect().addValueChangeListener(e -> editBook(e.getValue()));
    }

    private void configureForm() {
        form.setWidth("25em");
        form.addSaveListener(this::saveBook);
        form.addDeleteListener(this::deleteBook);
        form.addCloseListener(e -> closeEditor());
        closeEditor();
    }

    private void updateGrid() {
        String filter = filterText.getValue();
        if (filter == null || filter.isBlank()) {
            grid.setItems(bookRepository.findAll());
        } else {
            grid.setItems(bookRepository.findByTitleContainingIgnoreCase(filter));
        }
    }

    private void addBook() {
        grid.asSingleSelect().clear();
        editBook(new Book("", "", true));
    }

    private void editBook(Book book) {
        if (book == null) {
            closeEditor();
            return;
        }
        form.setBook(book);
        form.setVisible(true);
    }

    private void closeEditor() {
        form.setBook(null);
        form.setVisible(false);
    }

    private void saveBook(BookForm.SaveEvent event) {
        bookRepository.save(event.getBook());
        updateGrid();
        closeEditor();
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        bookRepository.delete(event.getBook());
        updateGrid();
        closeEditor();
    }
}
