import * as React from 'react';
import * as cn from 'classnames';
import './index.sass'

export interface InputProps {
    value: string;
    label: string;
    className?: string;
    onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

function Input(props: InputProps) {
    const {className, label, value, onChange} = props;
    const containerClassName = cn('app-input', className);
    return (
        <div className={containerClassName}>
            {label ? <label className='app-input__label'>{label}</label> : null}
            <input
                onChange={onChange}
                value={value}
                className='app-input__input'
            />
        </div>
    )
}

export default Input
