import * as React from 'react';
import * as cn from 'classnames';
import './index.sass';

export interface InputProps {
    onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
    placeholder?: string;
    label?: string;
    value?: string;
    className?: string;
    error?: boolean;
    style?: object;
}

function Input(props: InputProps) {
    const {className, label, value, onChange, error, style, ...otherProps} = props;
    const containerClassName = cn('app-input', className);
    const inputClassName = cn('app-input__input', {error}, className);
    return (
        <div className={containerClassName} style={style}>
            {label ? <label className='app-input__label'>{label}</label> : null}
            <input
                {...otherProps}
                onChange={onChange}
                value={value}
                className={inputClassName}
            />
        </div>
    )
}

export default Input
