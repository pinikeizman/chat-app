import React, {useState} from 'react';
import './index.sass'

function Button({label, type}) {
    return (
        <button className={`app-btn${type && '__'+ type}`}>{label}</button>
    )
}

export default Button
