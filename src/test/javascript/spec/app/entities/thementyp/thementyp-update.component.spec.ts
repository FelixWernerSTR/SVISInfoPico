/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ThementypUpdateComponent from '@/entities/thementyp/thementyp-update.vue';
import ThementypClass from '@/entities/thementyp/thementyp-update.component';
import ThementypService from '@/entities/thementyp/thementyp.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Thementyp Management Update Component', () => {
    let wrapper: Wrapper<ThementypClass>;
    let comp: ThementypClass;
    let thementypServiceStub: SinonStubbedInstance<ThementypService>;

    beforeEach(() => {
      thementypServiceStub = sinon.createStubInstance<ThementypService>(ThementypService);

      wrapper = shallowMount<ThementypClass>(ThementypUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          thementypService: () => thementypServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.thementyp = entity;
        thementypServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(thementypServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.thementyp = entity;
        thementypServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(thementypServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundThementyp = { id: 123 };
        thementypServiceStub.find.resolves(foundThementyp);
        thementypServiceStub.retrieve.resolves([foundThementyp]);

        // WHEN
        comp.beforeRouteEnter({ params: { thementypId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.thementyp).toBe(foundThementyp);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
