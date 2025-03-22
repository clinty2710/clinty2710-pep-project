package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::handleRegister);

        app.post("/login", this::handleLogin);

        app.post("/messages", this::handleCreateMessage);

        app.get("/messages", this::handleGetAllMessages);

        app.get("/messages/{message_id}", this::handleGetMessageById);

        app.delete("/messages/{message_id}", this::handleDeleteMessageById);

        app.patch("/messages/{message_id}", this::handleUpdateMessageText);

        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByAccountId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * process user registration
     */
    private void handleRegister(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account createdAccount = accountService.register(account);

        if (createdAccount != null) {
            context.json(createdAccount);
        } else {
            context.status(400);
        }
    }

    /**
     * process login request
     */
    private void handleLogin(Context context) {
        Account loginRequest = context.bodyAsClass(Account.class);
        Account loggedIn = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (loggedIn != null) {
            context.json(loggedIn);
        } else {
            context.status(401);
        }
    }

    /**
     * process message creation requests, save to db
     */
    private void handleCreateMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            context.json(createdMessage);
        } else {
            context.status(400);
        }
    }

    /**
     * return all messages from the db
     */
    private void handleGetAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    /**
     * returns message by message_id
     */
    private void handleGetMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message);
        } else {
            context.result("");
        }
    }

    /**
     * delete message by id
     */
    private void handleDeleteMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deleted = messageService.deleteMessageById(messageId);

        if (deleted != null) {
            context.json(deleted);
        } else {
            context.result("");
        }
    }

    /**
     * update message_text for specific message_id
     */
    private void handleUpdateMessageText(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message updateRequest = context.bodyAsClass(Message.class);
        String newText = updateRequest.getMessage_text();

        Message updated = messageService.updateMessageText(messageId, newText);

        if (updated != null) {
            context.json(updated);
        } else {
            context.status(400);
        }
    }

    /**
     * return messages posted by id
     */
    private void handleGetMessagesByAccountId(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        context.json(messages);
    }
}
