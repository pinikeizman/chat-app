import * as React from 'react';
import * as cn from 'classnames';
import './index.sass'

export interface ButtonProps {
    label: string;
    color: string;
    onClick: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
    className?: string;
    disabled?: boolean;
}

function Button(props: ButtonProps) {
    const {color, label, onClick, className, disabled, ...otherProps} = props;
    const btnClassName = cn('app-btn', {
            [`app-btn__${color}`]: color,
            disabled
        },
        className
    );
    return (
        <button
            {...otherProps}
            disabled={disabled}
            onClick={onClick}
            className={btnClassName}
        >
            {label}
        </button>
    )
}

export default Button
