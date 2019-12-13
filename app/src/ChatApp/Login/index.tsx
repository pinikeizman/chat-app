import * as React from 'react';
import './index.sass';
import Input from '../Input';
import Button from '../Button';
import ErrorList from "../ErrorList";

import {User, Errors} from "../../types";
import {Dispatch} from "react";
import {SetStateAction} from "react";

const INVALID_USER_ERROR_MSG: string = "Please enter a valid username";

export interface LoginProps {
    onLogin: (user: User, event: React.MouseEvent | React.KeyboardEvent) => void
}

function createOnChangeHandler(
    currentUser: User,
    setUser: Dispatch<SetStateAction<User>>,
    setErrors: Dispatch<SetStateAction<Errors>>
): (event: React.ChangeEvent<HTMLInputElement>) => void {
    return (event: React.ChangeEvent<HTMLInputElement>) => {
        const userName: string = event.target.value;
        !isValidUser({...currentUser, userName}) ?
            setErrors([INVALID_USER_ERROR_MSG]) :
            setErrors([]);
        setUser({...currentUser, userName})
    }
}

function createOnLoginHandler(
    user: User,
    onLogin: LoginProps["onLogin"],
    setErrors: Dispatch<SetStateAction<Errors>>
) {
    return (event: React.MouseEvent | React.KeyboardEvent) => {
        if (!isValidUser(user)) {
            setErrors([INVALID_USER_ERROR_MSG])
        } else {
            onLogin(user, event)
        }
    }
}

function isValidUser(user: User): boolean {
    return !(!user || !user.userName || user.userName.length === 0);
}

const Login: React.FC<LoginProps> = (props: LoginProps) => {
    const {onLogin} = props;
    const [user, setUser] = React.useState<User>({
        userName: ""
    });
    const [errors, setErrors] = React.useState<Errors>([]);
    const onLoginHandler = createOnLoginHandler(user, onLogin, setErrors);
    return (
        <>
            <div className='app-login-container'>
                <Input
                    placeholder='Type a nickname'
                    onChange={createOnChangeHandler(user, setUser, setErrors)}
                    error={errors.length > 0}
                    onKeyPress={(event: React.KeyboardEvent) => {
                        if (event.key === "Enter")
                            onLoginHandler(event)
                    }}
                />
                <Button
                    label='Login'
                    color={'submit'}
                    onClick={onLoginHandler}
                />
                {
                    errors.length > 0 && <ErrorList errors={errors}/>
                }
            </div>
        </>
    );
};

export default Login;
