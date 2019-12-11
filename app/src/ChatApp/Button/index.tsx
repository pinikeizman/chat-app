import * as React from 'react';
import * as cn from 'classnames';
import './index.sass'

export interface ButtonProps {
    label: string;
    color: string;
    onClick: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
    className?: string;
}

function Button(props: ButtonProps) {
    const {color, label, onClick, className, ...otherProps} = props;
    const btnClassName = cn('app-btn', {
            [`app-btn__${color}`]: color
        },
        className
    );
    return (
        <button
            {...otherProps}
            onClick={onClick}
            className={btnClassName}
        >
            {label}
        </button>
    )
}

export default Button
