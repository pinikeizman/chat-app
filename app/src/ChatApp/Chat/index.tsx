import * as React from 'react';
import './index.sass';
import Input from '../Input';
import Button from '../Button';
import {User} from "../../types";
import {Dispatch} from "react";
import {SetStateAction} from "react";

export interface LoginProps {
    onLogin: (user: User, event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void
}

function createOnChangeHandler(
    currentUser: User,
    setUser: Dispatch<SetStateAction<User>>
): (event: React.ChangeEvent<HTMLInputElement>) => void {
    return (event: React.ChangeEvent<HTMLInputElement>) => setUser({...currentUser, userName:event.target.value})
}
function createOnLoginHandler(user: User, onLogin: LoginProps["onLogin"]){
    return (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => onLogin(user, event)
}

const Login: React.FC<LoginProps> = (props: LoginProps) => {
    const {onLogin} = props;
    const [user, setUser] = React.useState<User>({
        userName: ""
    });

    return (
        <div>
            <div className='app-login-container'>
                <Input
                    value={user.userName}
                    label='Please choose a nickname:'
                    onChange={createOnChangeHandler(user, setUser)}
                />
                <Button
                    label='Login'
                    type={'submit'}
                    onClick={createOnLoginHandler(user, onLogin)}
                />
            </div>
        </div>
    );
};

export default Login;
