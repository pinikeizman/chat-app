import * as React from 'react';
import {Errors} from "../../types";
import './index.sass';

export interface ErrorListProps {
    errors: Errors
}

const ErrorList: React.FC<ErrorListProps> = (props: ErrorListProps) => {
    const {errors} = props;
    return (
        <ul className='app-error-list'>
            {
                errors.map(err => <li key={err} className={'app-error-list-item'}>{err}</li>)
            }
        </ul>
    );
};

export default ErrorList;
