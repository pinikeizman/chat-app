import React, {useState} from 'react';
import cn from 'classnames';
import './index.sass'

function Input({label, className, ...props}) {
    const containerClassName = cn('app-input', className);
    return (
        <div className={containerClassName}>
            {label ? <label className='app-input__label'>{label}</label> : null}
            <input
                {...props}
                className='app-input__input'
                />
        </div>
    )
}

export default Input
