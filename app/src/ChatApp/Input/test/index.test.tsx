import Input from '../index'
import { spy } from 'sinon';
import { expect } from 'chai'
import * as React from "react";
import {mount} from "enzyme";

describe('Input component', function () {
    it('Should display proper label when label prop is present', function () {
        const labelValue = 'Tiny Rick!';
        const wrapper = mount(<Input label={labelValue} onChange={null}/>);
        const label = wrapper.find('label');
        expect(label.text()).to.eql(labelValue);
    });
    it('Should invoke on change when input value changed', function () {
        const value = 'Pickle Rick!';
        const onChange = spy();
        const wrapper = mount(<Input label='' onChange={onChange}/>);
        const input = wrapper.find('input');

        input.simulate('focus');
        input.simulate('change', { target: { value } });
        expect(onChange.calledOnce).to.equal(true);
        expect(onChange.getCalls()[0].args[0].target.value).to.equal(value);

    });
});
