import Button from '../index'
import { spy } from 'sinon';
import { expect } from 'chai'
import * as React from "react";
import {mount} from "enzyme";

describe('Button component', function () {
    it('Should display proper label', function () {
        const labelValue = 'Push to call Mr Meeseeks!';
        const wrapper = mount(<Button label={labelValue} color={"submit"} onClick={null}/>);
        expect(wrapper.text()).to.equal(labelValue);
    });
    it('Should invoke onClick handler', function () {
        const onClick = spy();
        const wrapper = mount(<Button label='' color='submit' onClick={onClick}/>);
        const button = wrapper.find('button');

        button.simulate('click');
        expect(onClick.calledOnce).to.equal(true);
    });
});
