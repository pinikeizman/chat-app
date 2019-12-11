export type User = { userName: string }
export type Errors = string[]
export type Message = {
    from: User;
    message: string;
}
export type WSClient = {
    send: (msg: string) => void;
    close: () => void;
    // onMessage: (handler: (msg:Message) => void) => void;
}
