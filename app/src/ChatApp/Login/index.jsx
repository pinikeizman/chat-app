import React, { useState } from 'react';
import './index.sass';
import Input from '../Input';
import Button from '../Button';

function Login({ onLogin }) {
  return (
    <div>
      <div className='app-login-container'>
        <Input label='Please choose a nickname:'/>
        <Button label='Login' type={'submit'} onClick={onLogin}/>
      </div>
    </div>
  );
}

export default Login;
