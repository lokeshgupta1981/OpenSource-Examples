package com.howtodoinjava.vaadincrud.book;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class BookForm extends VerticalLayout {

    private final BeanValidationBinder<Book> binder = new BeanValidationBinder<>(Book.class);

    // Field names match the Book properties, so bindInstanceFields() can connect them
    private final TextField title = new TextField("Title");
    private final TextField author = new TextField("Author");
    private final Checkbox available = new Checkbox("Available");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private Book book;

    public BookForm() {
        binder.bindInstanceFields(this);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(e -> validateAndSave());
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, book)));
        cancel.addClickListener(e -> fireEvent(new CloseEvent(this)));

        add(title, author, available, new HorizontalLayout(save, delete, cancel));
    }

    public void setBook(Book book) {
        this.book = book;
        binder.readBean(book);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(book);
            fireEvent(new SaveEvent(this, book));
        } catch (ValidationException e) {
            // The @NotBlank messages are already shown under each field
        }
    }

    public static abstract class BookFormEvent extends ComponentEvent<BookForm> {
        private final Book book;

        protected BookFormEvent(BookForm source, Book book) {
            super(source, false);
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }

    public static class SaveEvent extends BookFormEvent {
        SaveEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public static class DeleteEvent extends BookFormEvent {
        DeleteEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public static class CloseEvent extends BookFormEvent {
        CloseEvent(BookForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
