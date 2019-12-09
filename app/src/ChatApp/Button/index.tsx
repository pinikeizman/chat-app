import * as React from 'react';
import './index.sass'

export interface ButtonProps {
    label: string;
    type: string;
    onClick: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
}

function Button(props: ButtonProps) {
    const {type, label, onClick} = props;
    return (
        <button
            onClick={onClick}
            className={`app-btn${type && '__' + type}`}
        >
            {label}
        </button>
    )
}

export default Button
